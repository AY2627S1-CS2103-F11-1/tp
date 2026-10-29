package seedu.address;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    private static final String VERSION = "v1.5";

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final Path USER_PREFS_FILE_PATH = Paths.get("preferences.json");
    private static final Path ADDRESS_BOOK_FILE_PATH = Paths.get("data", "addressbook.json");
    private static final String INVALID_COMPLETION_STATUS_MESSAGE =
            "Invalid completion status. Use DONE or NOT_DONE.";

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    private RuntimeException startupFailure;

    @Override
    public void init() throws Exception {
        logger.info("==============================[ Initializing TAssist ]==============================");
        super.init();

        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(USER_PREFS_FILE_PATH);
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(ADDRESS_BOOK_FILE_PATH);
        storage = new StorageManager(addressBookStorage, userPrefsStorage);

        try {
            model = initModelManager(storage, userPrefs);
        } catch (IllegalStateException e) {
            startupFailure = e;
            return;
        }

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic, storage.getAddressBookFilePath());
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The sample address book is used only when {@code storage}'s address book is missing.
     * Existing but invalid data prevents startup so that it cannot be overwritten by sample data.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getAddressBookFilePath());

        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialData;
        try {
            addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                logger.info("Creating a new data file " + storage.getAddressBookFilePath()
                        + " populated with a sample AddressBook.");
            }
            initialData = addressBookOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
        } catch (DataLoadingException e) {
            Path addressBookFilePath = storage.getAddressBookFilePath();
            if (!Files.notExists(addressBookFilePath)) {
                Throwable cause = e.getCause();
                String causeMessage = cause == null || cause.getMessage() == null
                        ? "No further details are available."
                        : cause.getMessage();
                int sourcePreviewStart = causeMessage.indexOf(" at [Source:");
                if (sourcePreviewStart >= 0) {
                    causeMessage = causeMessage.substring(0, sourcePreviewStart);
                }
                causeMessage = causeMessage.trim();
                if (causeMessage.contains("CompletionStatus")) {
                    causeMessage = INVALID_COMPLETION_STATUS_MESSAGE;
                }
                if (causeMessage.length() > 500) {
                    causeMessage = causeMessage.substring(0, 500) + "...";
                }
                String failureType = cause instanceof IllegalValueException
                        ? "The saved data is invalid."
                        : "The saved data could not be read.";
                throw new IllegalStateException(failureType + "\n\n" + causeMessage
                        + "\n\nThe original file was not modified. Restore a backup or correct the file,"
                        + " then restart TAssist.", e);
            }
            logger.warning("Data file at " + addressBookFilePath
                    + " could not be loaded because it is missing."
                    + " Will be starting with sample student data.");
            initialData = SampleDataUtil.getSampleAddressBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(JsonUserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (prefsOptional.isEmpty()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save preference file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting TAssist " + MainApp.VERSION);
        if (startupFailure != null) {
            showStartupFailure(startupFailure);
            return;
        }
        ui.start(primaryStage);
    }

    private void showStartupFailure(RuntimeException failure) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to load saved data");
        alert.setHeaderText("TAssist could not load your existing data file.");
        alert.setContentText(failure.getMessage());
        alert.setGraphic(null);
        alert.getDialogPane().setMinWidth(650);
        alert.getDialogPane().setPrefWidth(700);
        alert.showAndWait();
        Platform.exit();
    }

    @Override
    public void stop() {
        logger.info("============================== [ Stopping TAssist ] ===============================");
        if (model == null) {
            return;
        }
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
