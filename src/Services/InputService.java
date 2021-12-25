package Services;

import Exceptions.CustomException;
import Models.UserModel;
import Views.OutputView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.swing.*;


/**
 * Java inner class for actions
 */
public class InputService {

    private final DataBaseService DBService;
    private final UserModel UModel;
    private final UserService UService;
    private boolean deleteStatus;

    public InputService(DataBaseService DBService, UserModel UModel, UserService UService) {
        this.DBService = DBService;
        this.UModel = UModel;
        this.UService = UService;
    }

    /**
     * Check lines to sign up
     */
    public void Try_SignUp(String login, String password, String passwordRepeat) throws CustomException, SQLException, ClassNotFoundException {

            Check_NoEmptyFieldsExist(login, password, passwordRepeat);

            Check_PasswordMatch(password, passwordRepeat);

            DBService.SignUp(login,password);
    }

    public void DisplayErrorMessage(String error)
    {
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void Check_NoEmptyFieldsExist(String @NotNull ... inputFields) throws CustomException {
        for (String field : inputFields) {
            if (field.isEmpty()) throw new CustomException("Pls fill all the fields");
            if (field.length() <= 3)
                throw new CustomException("Login and Password should be at least 3 characters");
        }
    }

    public void Check_PasswordMatch(@NotNull String password, String passwordRepeat) throws CustomException {
        if (!password.equals(passwordRepeat))
            throw new CustomException("Password doesnt match");
    }

    public void Try_SignIn(String login, String password) throws CustomException, SQLException, ClassNotFoundException {

            Check_NoEmptyFieldsExist(login, password);
            DBService.SignIn(login, password);

    }

    public void Try_RunMedia(String mediaName) {
        try {
            DBService.RunMedia(mediaName);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            System.out.println(e);
        }
    }

    public String[] Try_AddMedia(DefaultListModel<String> listModel) {
        String[] mediaData = new String[]{};
        try {
            mediaData = GetDataFromFile(listModel);
            DBService.AddMedia(mediaData);
        } catch (SQLException | ClassNotFoundException | NullPointerException | CustomException | IOException e) {
            DisplayErrorMessage("Cannot add");
        }
        return mediaData;
    }

    @Contract("_ -> new")
    public String @NotNull [] GetDataFromFile(DefaultListModel<String> listModel) throws NullPointerException, CustomException {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);

        if (chooser.getSelectedFile() == null)
            throw new NullPointerException("No file has been selected");

        if (listModel.contains(chooser.getSelectedFile().getName()))
            throw new CustomException("This file name already exist in the list");

        FileInputStream fis;
        byte[] readedFile = new byte[0];

        try {
            fis = new FileInputStream(chooser.getSelectedFile().getPath());
            readedFile = fis.readAllBytes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String byteString = new String(readedFile, StandardCharsets.UTF_8);

        return new String[]{
                chooser.getSelectedFile().getName(),
//                byteString,
                UModel.GetLogin(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "\\\\\\\\")
        };
    }

    //    public String Try_EditMedia(String selectedMediaName, String[] listContent) {
//        String newMediaName = "";
//        try {
//            newMediaName = EditMedia(selectedMediaName, GetNewMediaNameInput(listContent));
//        } catch (SQLException | ClassNotFoundException | EmptyInputFieldException |
//                MediaNameAlreadyExistException | IOException | RenamedFileException e) {
//            DisplayError(e.getMessage());
//        }
//        return newMediaName;
//    }

    public boolean isDeleteStatus() {
        return deleteStatus;
    }

    public void checkChangeStatus(String login, String mediaName){
        deleteStatus = Objects.equals(login, mediaName);
        if (!deleteStatus)
            DisplayErrorMessage("You do not have permissions");
    }

    public void Try_DeleteMedia(String mediaName, String login) {

        try {
            DBService.DeleteMedia(mediaName, login);
        } catch (SQLException | ClassNotFoundException | CustomException e) {
            DisplayErrorMessage("You do not have permissions to do this");
        }

    }

//    public String GetMediaName(String[] listContent) {
//        String newMediaName = DisplayInputDialog();
//
//        if (newMediaName == null || newMediaName.trim().isEmpty())
//            throw new EmptyInputFieldException("Invalid name !");
//
//        for (String name : listContent) {
//            if (name.equals(newMediaName))
//                throw new MediaNameAlreadyExistException("There is already an item with the same name");
//        }
//        return newMediaName;
//    }

//    public String DisplayInputDialog() {
//        return showInputDialog(null, "Enter the new item name",
//                "Editing item", PLAIN_MESSAGE);
//    }


}
