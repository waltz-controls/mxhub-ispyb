package ispyb;

import jakarta.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Properties;

public class TestBase {

    protected static EJBContainer ejbContainer;

    @BeforeClass
    public static void beforeClass() {
        Properties properties = new Properties();
//        properties.put("jdbc/ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb.JdbcDriver", "org.mariadb.jdbc.Driver");
        properties.put("ispyb.JdbcUrl", "jdbc:mariadb://localhost:3306/pydb");
        properties.put("ispyb.UserName", "pxadmin");
        properties.put("ispyb.Password", "pxadmin");
        ejbContainer = EJBContainer.createEJBContainer(properties);
    }

    @AfterClass
    public static void afterClass() {
        if(ejbContainer != null)
            ejbContainer.close();
    }

    @Before
    public void before() throws Exception {
        ejbContainer.getContext().bind("inject", this);

    }

    @After
    public void after() throws Exception {
        ejbContainer.getContext().unbind("inject");
    }
}
