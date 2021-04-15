package Services;

import Entities.Ingredient;
import Entities.Ingredient;
import Entities.Ingredient;
import tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientService {

    public void save(Ingredient ingredient) {
        try{
            String request = "INSERT INTO ingredient (description,menu_id)" +
                    " VALUES (?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            pst.setString(1, ingredient.getDescription());
            pst.setInt(2, ingredient.getMenu().getId());
            pst.executeUpdate();
            System.out.println("Ingredient Saved!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public int delete(int id) {
        try {
            String request = "Delete FROM ingredient where id = " + id;
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            System.out.println("ingredient deleted !");
            return pst.executeUpdate(request);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public int update(Ingredient ingredient) {
        try {
            String request = "UPDATE ingredient set description = ?, menu_id = ?   WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            pst.setString(1, ingredient.getDescription());
            pst.setInt(2, ingredient.getMenu().getId());
            pst.setInt(3, ingredient.getId());
            System.out.println("ingredient Updated !");
            return pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public List<Ingredient> findAll(){
        List<Ingredient> myList = new ArrayList<>();

        try {
            String request = "SELECT * FROM Ingredient";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet resultSet = st.executeQuery(request);
            while(resultSet.next()) {
                Ingredient ingredient = new Ingredient(resultSet.getInt("id"),
                        resultSet.getString("description"),
                         new MenuService().findById(resultSet.getInt("menu_id")));
                myList.add(ingredient);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }
}
