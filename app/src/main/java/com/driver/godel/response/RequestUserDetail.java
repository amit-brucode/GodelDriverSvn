package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 11/7/17.
 */

public class RequestUserDetail
{
    private String user_app_notification;

    private String user_status;

    private String user_email;

    private String password;

    private String id;

    private String updated_at;

    private String validate_code;

    private String source;

    private String token;

    private String user_phone;

    private String name;

    private String created_at;

    private String user_image;

    private String login_type;

    public String getUser_app_notification ()
    {
        return user_app_notification;
    }

    public void setUser_app_notification (String user_app_notification)
    {
        this.user_app_notification = user_app_notification;
    }

    public String getUser_status ()
    {
        return user_status;
    }

    public void setUser_status (String user_status)
    {
        this.user_status = user_status;
    }

    public String getUser_email ()
    {
        return user_email;
    }

    public void setUser_email (String user_email)
    {
        this.user_email = user_email;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getValidate_code ()
    {
        return validate_code;
    }

    public void setValidate_code (String validate_code)
    {
        this.validate_code = validate_code;
    }

    public String getSource ()
    {
        return source;
    }

    public void setSource (String source)
    {
        this.source = source;
    }

    public String getToken ()
{
    return token;
}

    public void setToken (String token)
    {
        this.token = token;
    }

    public String getUser_phone ()
    {
        return user_phone;
    }

    public void setUser_phone (String user_phone)
    {
        this.user_phone = user_phone;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getUser_image ()
    {
        return user_image;
    }

    public void setUser_image (String user_image)
    {
        this.user_image = user_image;
    }

    public String getLogin_type ()
    {
        return login_type;
    }

    public void setLogin_type (String login_type)
    {
        this.login_type = login_type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_app_notification = "+user_app_notification+", user_status = "+user_status+", user_email = "+user_email+", password = "+password+", id = "+id+", updated_at = "+updated_at+", validate_code = "+validate_code+", source = "+source+", token = "+token+", user_phone = "+user_phone+", name = "+name+", created_at = "+created_at+", user_image = "+user_image+", login_type = "+login_type+"]";
    }
}
