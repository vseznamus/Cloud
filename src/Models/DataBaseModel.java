package Models;

import org.jetbrains.annotations.NotNull;

/*
Java class for database description
 */
public final class DataBaseModel
{
    private String url = "jdbc:mysql://localhost:3306/myDatabase?autoReconnect=true&useSSL=false";
    private String login = "root";
    private String password = "99#dfgDD56678";

    public void SetUrl(String URL)
    {
        this.url = URL;
    }

    public void SetLogin(String LOGIN)
    {
        this.login = LOGIN;
    }

    public void SetPassword(String PASSWORD)
    {
        this.password = PASSWORD;
    }

    public String GetUrl()
    {
        return this.url;
    }

    public String GetLogin()
    {
        return this.login;
    }

    public String GetPassword()
    {
        return this.password;
    }
}
