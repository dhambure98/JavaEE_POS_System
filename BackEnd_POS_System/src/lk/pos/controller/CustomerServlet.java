package lk.pos.controller;

import javafx.collections.ObservableList;
import lk.pos.bo.BOFactory;
import lk.pos.bo.custom.CustomerBO;
import lk.pos.dto.CustomerDTO;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            CustomerDTO customerDTO = new CustomerDTO(
                    req.getParameter("customerID"),
                    req.getParameter("customerName"),
                    req.getParameter("customerAddress"),
                    req.getParameter("customerContact")
            );

            try {
                if (customerBO.addCustomer(connection, customerDTO)){
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Added");
                    objectBuilder.add("data", "");
                    writer.print(objectBuilder.build());
                }
            } catch (ClassNotFoundException e) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Error");
                objectBuilder.add("data", e.getLocalizedMessage());
                writer.print(objectBuilder.build());
                resp.setStatus(HttpServletResponse.SC_OK);
                e.printStackTrace();
                e.printStackTrace();
            }

            connection.close();

        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            resp.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String option = req.getParameter("option");
            String customerID = req.getParameter("customerID");
            resp.setContentType("application/json");
            Connection connection = dataSource.getConnection();
            PrintWriter writer = resp.getWriter();


            switch (option){
                case "SEARCH":

                    CustomerDTO customer = customerBO.searchCustomer(customerID, connection);
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                    objectBuilder.add("id", customer.getCusId());
                    objectBuilder.add("name", customer.getCusName());
                    objectBuilder.add("address", customer.getCusAddress());
                    objectBuilder.add("contact", customer.getCusContact());

                    writer.print(objectBuilder.build());

                    break;

                case "GETALL":

                    ObservableList<CustomerDTO> allCustomers = customerBO.getAllCustomer(connection);
                    JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();

                    for (CustomerDTO cust : allCustomers){

                        JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();
                        objectBuilder1.add("id", cust.getCusId());
                        objectBuilder1.add("name", cust.getCusName());
                        objectBuilder1.add("address", cust.getCusAddress());
                        objectBuilder1.add("contact", cust.getCusContact());
                        arrayBuilder1.add(objectBuilder1.build());

                    }

                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 200);
                    response1.add("message", "Done");
                    response1.add("data", arrayBuilder1.build());
                    writer.print(response1.build());

                    break;

                case "COUNT":
                    writer.print(customerBO.countCustomer(connection));
                    break;
            }

            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String customerID = req.getParameter("customerID");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            if (customerBO.deleteCustomer(connection, customerID)){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status",200);
                objectBuilder.add("data","");
                objectBuilder.add("message","Successfully Deleted");
                writer.print(objectBuilder.build());
            }else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("data", "Wrong Id Inserted");
                objectBuilder.add("message", "");
                writer.print(objectBuilder.build());
            }

            connection.close();

        } catch (SQLException e) {

            resp.setStatus(200);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();

        } catch (ClassNotFoundException e) {

            resp.setStatus(200);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {
            Connection connection = dataSource.getConnection();

            CustomerDTO customerDTO = new CustomerDTO(
                    jsonObject.getString("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("address"),
                    jsonObject.getString("contact")
            );

            if (customerBO.updateCustomer(connection, customerDTO)){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Updated");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }else{
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }

            connection.close();

        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Update Failed");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Update Failed");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();
        }
    }

}