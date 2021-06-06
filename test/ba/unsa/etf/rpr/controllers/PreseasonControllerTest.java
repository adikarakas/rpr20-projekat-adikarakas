package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.beans.Club;
import ba.unsa.etf.rpr.other.LeagueDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class PreseasonControllerTest {
    LeagueDAO dao = LeagueDAO.getInstance();
    Stage theStage;
    PreseasonController ctrl;
    public Button addClubButton;

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/preseason.fxml"), bundle);
        ctrl = new PreseasonController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Preseason");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @BeforeEach
    public void resetujBazu() throws SQLException {
        dao.vratiBazuNaDefault();
    }

    @Test
    public void testListView(FxRobot robot) throws SQLException {
        ListView listView = robot.lookup("#clubsLv").queryAs(ListView.class);
        assertEquals(4, listView.getItems().size());
        dao.vratiBazuNaDefault();
    }

    @Test
    public void deleteClub(FxRobot robot) throws SQLException {
        robot.clickOn("Everton");
        robot.clickOn("#deleteClubButton");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertEquals(3, dao.clubs().size());
        dao.vratiBazuNaDefault();
    }

    @Test
    public void addClub(FxRobot robot) throws SQLException {
        robot.clickOn("#addClubButton");

        robot.lookup("#nameField").tryQuery().isPresent();

        robot.clickOn("#nameField");
        robot.write("Southampton");
        robot.clickOn("#okButton");

        assertEquals(5, dao.clubs().size());

        boolean find = false;
        for(Club club : dao.clubs()) {
            if (club.getName().equals("Southampton")) {
                find = true;
                break;
            }
        }
        assertTrue(find);
        dao.vratiBazuNaDefault();
    }

    @Test
    public void addClubOverflow(FxRobot robot) throws SQLException {
        for (int i=0; i<26; i++) {
           dao.addClub(new Club(("Southampton" + (i+1))));
        }

        robot.clickOn("#addClubButton");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane.lookupAll("Ne možete dodavati nove timove u ligu"));
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        dao.vratiBazuNaDefault();
    }

    @Test
    public void startWithOddNumberOfClubs(FxRobot robot) throws SQLException {
        robot.clickOn("Everton");
        robot.clickOn("#deleteClubButton");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("#startButton");
        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane2.lookupAll("Liga mora imati paran broj klubova"));
        okButton = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        dao.vratiBazuNaDefault();
    }

    @Test
    public void startWithInsufficientNumberOfClubs(FxRobot robot) throws SQLException {
       // ListView listView = robot.lookup("#clubsLv").queryAs(ListView.class);

        robot.clickOn("Everton");
        robot.clickOn("#deleteClubButton");
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("Chelsea");
        robot.clickOn("#deleteClubButton");
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("Arsenal");
        robot.clickOn("#deleteClubButton");
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("#startButton");
        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane2.lookupAll("Liga mora imati barem 2 kluba"));

        okButton = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        dao.vratiBazuNaDefault();
    }

    @Test
    public void startWithoutFullSquads (FxRobot robot) throws SQLException {
        robot.clickOn("Everton");
        robot.clickOn("#editClubButton");

        robot.lookup("#nameField").tryQuery().isPresent();

        robot.clickOn("Allan");
        robot.clickOn("#removePlayerButton");

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.clickOn("#okButton");

        robot.lookup("#addClubButton").tryQuery().isPresent();
        robot.clickOn("#startButton");
        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane2.lookupAll("Klub Everton ima manje od 15 igrača"));

        okButton = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        dao.vratiBazuNaDefault();
    }

    @Test
    public void startWithoutFullSquadsByPositions (FxRobot robot) throws SQLException {
        robot.clickOn("Chelsea");
        robot.clickOn("#editClubButton");

        robot.lookup("#nameField").tryQuery().isPresent();

        robot.clickOn("Edouard Mendy");
        robot.clickOn("#removePlayerButton");

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("Kepa Arrizabalaga");
        robot.clickOn("#removePlayerButton");

        dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.clickOn("#okButton");

        robot.lookup("#addClubButton").tryQuery().isPresent();
        robot.clickOn("#startButton");
        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane2.lookupAll("Klub Chelsea nema dovoljno igrača po pozicijama."));

        okButton = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        dao.vratiBazuNaDefault();
    }

    @Test
    public void startWithoutManager(FxRobot robot) throws SQLException {
        robot.clickOn("Chelsea");
        robot.clickOn("#editClubButton");
        robot.lookup("#nameField").tryQuery().isPresent();
        robot.clickOn("#managerField");
        robot.eraseText(13);
        robot.clickOn("#okButton");
        robot.lookup("#addClubButton").tryQuery().isPresent();
        robot.clickOn("#startButton");
        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane2.lookupAll("Klub Chelsea nije imenovao menadžera."));
        Button okButton = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        dao.vratiBazuNaDefault();
    }

}