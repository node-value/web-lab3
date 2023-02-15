package nick_snt1.lab;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import nick_snt1.lab.util.AreaChecker;

@ManagedBean @SessionScoped
public class PointBean implements Serializable {
    
    @Inject private HttpSession session;
    
    private String sessionId;

    @Getter @Setter private      Point  point;
    @Getter @Setter private List<Point> points;

    private Connection conn;

    @PostConstruct
    public void init() throws SQLException { 
        point = new Point();
        points = new LinkedList<>();
        sessionId = session.getId();
        connect();
        loadPoints(); 
    }

    private void connect() {
        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup("java:/RootDS");
            conn = dataSource.getConnection();
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS points (" +
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
    public void loadPoints() throws SQLException {
        try {
            conn.setAutoCommit(false); conn.setSavepoint();
            PreparedStatement stmt = conn.prepareStatement( "SELECT * FROM points WHERE id = ?");
            stmt.setString(1, sessionId);
            ResultSet res = stmt.executeQuery();
            while (res.next()) points.add( new Point(res.getDouble("x"), res.getDouble("y"), res.getInt("r"), res.getString("hit")));
        } catch (Exception e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public String addPoint() throws SQLException {
        try {
            conn.setAutoCommit(false); conn.setSavepoint();
            point.setHit(AreaChecker.isHit(point.getX(), point.getY(), point.getR().doubleValue()) ? "Hit" : "Miss");

            PreparedStatement stmt = conn.prepareStatement( "INSERT INTO points VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, sessionId);
            stmt.setDouble(2, point.getX());
            stmt.setDouble(3, point.getY());
            stmt.setInt   (4, point.getR());
            stmt.setString(5, point.getHit());
            stmt.execute();

            points.add(point);
            point = new Point();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return "redirect";
    } 

    public String clearPoints() throws SQLException {
        try {
            conn.setAutoCommit(false); conn.setSavepoint();
            PreparedStatement stmt = conn.prepareStatement( "DELETE FROM points WHERE id = ?");
            stmt.setString(1, sessionId);
            stmt.execute();
            points.clear();
        } catch (Exception e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return "redirect";
    }


}
