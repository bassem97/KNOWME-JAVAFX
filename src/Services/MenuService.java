package Services;

import Entities.*;
import Entities.Menu;
import Entities.Menu;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuService {


    public void save(Menu menu) {
//        if(emailExist(menu.getEmail())){
//            throw new ColumnExistException(
//                    "There is an account with that email adress:" + menu.getEmail());
//        }
        try{
            String request = "INSERT INTO menu (name, description, img,is_expired,categorie_id, date,expiration_date)" +
                    " VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            pst.setString(1, menu.getName());
            pst.setString(2, menu.getDescription());
            pst.setString(3, menu.getImg());
            pst.setBoolean(4, menu.getIs_expired());
            pst.setInt(5, menu.getCategorie().getId());
            pst.setDate(6, Date.valueOf(menu.getCreation_date().toLocalDate()));
            pst.setDate(7, Date.valueOf(menu.getExpiration_date().toLocalDate()));
            pst.executeUpdate();
            System.out.println("Menu Saved!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public int delete(int id) {
        try {
            String request = "Delete FROM menu where id = " + id;
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            System.out.println("menu deleted !");
            return pst.executeUpdate(request);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public int update(Menu menu) {
        try {
            String request = "UPDATE menu set name = ?, description = ?, img = ?, is_expired = ?, categorie_id = ?   WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            pst.setString(1, menu.getName());
            pst.setString(2, menu.getDescription());
            pst.setString(3, menu.getImg());
            pst.setBoolean(4, menu.getIs_expired());
            System.out.println(menu.getCategorie());
            pst.setInt(5, menu.getCategorie().getId());
            pst.setInt(6, menu.getId());

            System.out.println("menu Updated !");
            return pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public Menu findById(int id){
        Menu menu = null;
        String req = "SELECT * FROM menu WHERE id LIKE '"+id+"'";
        try {
            Statement statement = MyConnection.getInstance().getCnx().createStatement();
            ResultSet resultSet = statement.executeQuery(req);
            if(resultSet.next()){
                menu = new Menu(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        new CategorieService().findById(resultSet.getInt("categorie_id"))
                );
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return  null;
        }
        return menu;
    }
    
    public List<Menu> findAll(){
        List<Menu> myList = new ArrayList<>();

        try {
            String request = "SELECT * FROM Menu";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet resultSet = st.executeQuery(request);
            while(resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(resultSet.getInt("id"));
                menu.setName(resultSet.getString("name"));
                menu.setDescription(resultSet.getString("description"));
                menu.setImg(resultSet.getString("img"));
                menu.setCategorie(new CategorieService().findById(resultSet.getInt("categorie_id")) );
//                menu.setIngredients((ArrayList<Menu>) resultSet.getArray("ingredients"));

                myList.add(menu);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<Menu> findByCategorie(Categorie categorie) {
        try {
            String request = "SELECT * FROM menu where categorie_id = "+ categorie.getId();
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            ResultSet rs = pst.executeQuery(request);
            List<Menu> menus = new ArrayList<>();
            while (rs.next())
                menus.add(new Menu(rs.getString("name"),rs.getString("description")));
            return menus;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
