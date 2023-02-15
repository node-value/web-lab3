package nick_snt1.lab;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import lombok.*;
import nick_snt1.lab.util.AreaChecker;

@Named(value = "pointBean") @SessionScoped 
public class PointBean implements Serializable {
    
    @Inject private HttpSession session;
    
    private String sessionId;

    @Getter @Setter private      Point  point;
    @Getter @Setter private List<Point> points;

    private Connection conn;

    @PostConstruct
    public void init() { 
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
            //conn.createStatement().execute("DROP TABLE points");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS result_table (" +
                "id  TEXT             NOT NULL," +
                "x   DOUBLE PRECISION NOT NULL," +
                "y   DOUBLE PRECISION NOT NULL," +
                "r   INTEGER          NOT NULL," +
                "hit TEXT             NOT NULL" +
            ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadPoints() {
        try {
            if (conn == null) connect();
            System.out.println("Inside load points");
            PreparedStatement stmt = conn.prepareStatement( "SELECT * FROM result_table WHERE id = ?");
            stmt.setString(1, sessionId);
            ResultSet res = stmt.executeQuery();
            while (res.next()) points.add( new Point(res.getDouble("x"), res.getDouble("y"), res.getInt("r"), res.getString("hit")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPoint() {
        try {
            if (conn == null) connect();
            System.out.println("Inside add point");
      
            point.setHit(AreaChecker.isHit(point.getX(), point.getY(), point.getR().doubleValue()) ? "Hit" : "Miss");
            System.out.println("Points: " + point.getX() + " " + point.getY() + " " + point.getR());
            PreparedStatement stmt = conn.prepareStatement( "INSERT INTO result_table VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, sessionId);
            stmt.setDouble(2, point.getX());
            stmt.setDouble(3, point.getY());
            stmt.setInt   (4, point.getR());
            stmt.setString(5, point.getHit());
            stmt.execute();

            System.out.println("Statement exec");

            points.add(point);
            System.out.println(points.toString());
            point = new Point();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public void clearPoints() {
        try {
            if (conn == null) connect();
            System.out.println("Inside clear points");

            PreparedStatement stmt = conn.prepareStatement( "DELETE FROM result_table WHERE id = ?");
            
            stmt.setString(1, sessionId);
            stmt.execute();
            points.clear();

            System.out.println("Exec stmt clear");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
