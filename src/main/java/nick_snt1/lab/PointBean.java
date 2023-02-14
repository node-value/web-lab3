package nick_snt1.lab;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@ManagedBean @SessionScoped
public class PointBean implements Serializable {
    
    @Inject private HttpSession session;
    
    private String sessionId;

    private      Point  point;
    private List<Point> points;

    private Connection connection;

    @PostConstruct
    public void init() { 
        point = new Point();
        points = new ArrayList<>();
        sessionId = session.getId();
        connect(); 
    }

    private void connect() {
        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup("java:/RootDS");
            connection = dataSource.getConnection();
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS points (" +
                "id  TEXT             PRIMARY KEY," +
                "x   DOUBLE PRECISION NOT NULL," +
                "y   DOUBLE PRECISION NOT NULL," +
                "r   INTEGER          NOT NULL," +
                "hit TEXT             NOT NULL," +
            ")");
        } catch (Exception e) {
            throw new IllegalStateException("Connection to database failed", e);
        }
    }


}
