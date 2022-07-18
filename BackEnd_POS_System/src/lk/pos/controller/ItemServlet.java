package lk.pos.controller;

import javafx.collections.ObservableList;
import lk.pos.bo.BOFactory;
import lk.pos.bo.custom.ItemBO;
import lk.pos.dto.ItemDTO;

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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{

            String option = req.getParameter("option");
            String code = req.getParameter("itemCode");
            resp.setContentType("application/json");
            Connection connection = dataSource.getConnection();
            PrintWriter writer = resp.getWriter();

            switch (option){
                case "SEARCH":

                    ItemDTO itemDTO1 = itemBO.searchItem(code, connection);
                    JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();

                    objectBuilder1.add("itemCode", itemDTO1.getItemCode());
                    objectBuilder1.add("name", itemDTO1.getItemName());
                    objectBuilder1.add("qtyOnHand", itemDTO1.getQtyOnHand());
                    objectBuilder1.add("price", itemDTO1.getUnitPrice());

                    writer.print(objectBuilder1.build());

                    break;

                case "GETALL":

                    ObservableList<ItemDTO> allItem = itemBO.getAllItem(connection);
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    for (ItemDTO itemDTO : allItem){

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("itemCode", itemDTO.getItemCode());
                        objectBuilder.add("name", itemDTO.getItemName());
                        objectBuilder.add("qtyOnHand", itemDTO.getQtyOnHand());
                        objectBuilder.add("price", itemDTO.getUnitPrice());
                        arrayBuilder.add(objectBuilder.build());

                    }

                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 200);
                    response1.add("message", "Done");
                    response1.add("data", arrayBuilder.build());
                    writer.print(response1.build());

                    break;

                case "COUNT":

                    writer.print(itemBO.countItem(connection));

                    break;
            }

            connection.close();

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {
            Connection connection = dataSource.getConnection();
            ItemDTO itemDTO = new ItemDTO(
                    jsonObject.getString("itemCode"),
                    jsonObject.getString("itemName"),
                    Integer.parseInt(jsonObject.getString("itemQty")),
                    Double.parseDouble(jsonObject.getString("itemPrice"))
            );

            System.out.println(itemDTO);

            if (itemBO.addItem(connection, itemDTO)){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Added");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
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

        } catch (ClassNotFoundException e) {

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String itemCode = req.getParameter("itemCode");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Connection connection = dataSource.getConnection();

            if (itemBO.deleteItem(connection, itemCode)){
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
            e.printStackTrace();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();


        PrintWriter writer = resp.getWriter();
        resp.setContentType("Application/json");

        try {
            Connection connection = dataSource.getConnection();
            ItemDTO itemDTO = new ItemDTO(
                    jsonObject.getString("itemCode"),
                    jsonObject.getString("itemName"),
                    Integer.parseInt(jsonObject.getString("itemQty")),
                    Double.parseDouble(jsonObject.getString("itemPrice"))
            );

            if (itemBO.updateItem(connection, itemDTO)){
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

