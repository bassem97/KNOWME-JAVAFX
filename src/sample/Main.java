package sample;

import Entities.Categorie;
import Entities.Ingredient;
import Entities.Menu;
import Services.CategorieService;
import Services.IngredientService;
import Services.MenuService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        //*       TEST          *//
        Categorie categorie = new Categorie("saveTest","SAVETEST");
        CategorieService categorieService = new CategorieService();
        MenuService menuService = new MenuService();
        IngredientService ingredientService = new IngredientService();

        categorieService.save(categorie);
        List<Categorie> categories = categorieService.findAll();
        System.out.println(categories);

        Categorie willUpdate =  categories.stream()
                .filter(categorie1 -> categorie.getNom().equals(categorie1.getNom()))
                .findAny()
                .orElse(null)
                ;
        willUpdate.setNom("UpdateTest");
        categorieService.update(willUpdate);
        categorieService.delete(40);

        Menu menu = new Menu("salade","SALLLLAAAAAAADEEEEE",willUpdate);
        System.out.println(menu);
        menuService.save(menu);

        List<Menu> menus = menuService.findAll();
        System.out.println(menus);

        Menu menuWillUpdate =  menus.stream()
                .filter(menu1 -> menu.getName().equals(menu1.getName()))
                .findAny()
                .orElse(null)
                ;
        menuWillUpdate.setName("menu name after update !!");
        menuService.update(menuWillUpdate);
        menuService.delete(28);
        menuService.delete(27);

        Ingredient ingredient = new Ingredient("ingredient 1 ",menuWillUpdate);
        ingredientService.save(ingredient);

        List<Ingredient> ingredients = ingredientService.findAll();
        System.out.println(ingredients);

        Ingredient ingredientWillUpdate =  ingredients.stream()
                .filter(ingredient1 -> ingredient.getDescription().equals(ingredient1.getDescription()))
                .findAny()
                .orElse(null)
                ;

        ingredientWillUpdate.setDescription("ingredient after update");
        ingredientService.update(ingredientWillUpdate);




//        categorie.setNom("UpdateTest");
//        categorieService.update(categorie);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
