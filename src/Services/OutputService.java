package Services;

import Exceptions.CustomException;
import Models.DataBaseModel;
import Views.OutputView;

import java.sql.SQLException;

import Services.DataBaseService.*;

public class OutputService
{

    private final DataBaseService DBService;

    public OutputService(DataBaseService DBService)
    {
        this.DBService = DBService;
    }

    public String[] Try_FillList() {
        String[] listData = new String[]{};
        try{
            listData = DBService.GetMediaList();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return listData;
    }

    public String[] Try_GetMediaDescription(String selectedValue) {
        String[] mediaDescription = {"", "", "", ""};
        try {
            mediaDescription = DBService.GetMediaDescription(selectedValue);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return mediaDescription;
    }


    public String GetCurrentUser(UserService UService)
    {
        return UService.GetCurrentUser();
    }


}
