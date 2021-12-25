package Services;

import Models.DataBaseModel;
import Models.UserModel;
import Views.OutputView;

public class MainService
{
    private final DataBaseModel DBModel = new DataBaseModel();
    private final UserModel UModel = new UserModel();



    private final UserService UService = new UserService(UModel);
    private final DataBaseService DBService = new DataBaseService(DBModel,UService);

    private final OutputService OService = new OutputService(DBService);
    private final InputService IService = new InputService(DBService, UModel, UService);


    private final OutputView OView = new OutputView(UService,IService,OService,DBService,UModel);
//    private final FileSharingServlet fileSharingServlet = new FileSharingServlet(UService,IService,OService,DBService,UModel);

    public void start()
    {
        DBService.SetupDataBaseConnection();
        OView.SetUpGUI();
    }
}
