package com.nadia.recipes.Model;

import java.io.Serializable;

public class Meal implements Serializable {

   public String idMeal;
   public String strMeal;
   public String strCategory;
   public String strInstructions;
   public String strMealThumb;
   public String strYoutube;
   public String strIngredient1,strIngredient2,strIngredient3,strIngredient4,strIngredient5,strIngredient6,strIngredient7,strIngredient8,strIngredient9,strIngredient10;
   public String strIngredient11,strIngredient12,strIngredient13,strIngredient14,strIngredient15,strIngredient16,strIngredient17,strIngredient18,strIngredient19,strIngredient20;
   public String strMeasure1,strMeasure2,strMeasure3,strMeasure4,strMeasure5,strMeasure6,strMeasure7,strMeasure8,strMeasure9,strMeasure10;
   public String strMeasure11,strMeasure12,strMeasure13,strMeasure14,strMeasure15,strMeasure16,strMeasure17,strMeasure18,strMeasure19,strMeasure20;


   public String[] ingredientsArr;
   public String[] measuresArr;

   public Meal() {
   }

   public Meal(String strCategory,String idMeal,String strMeal,String strMealThumb){
      this.idMeal=idMeal;
      this.strMeal=strMeal;
      this.strMealThumb=strMealThumb;
      this.strCategory=strCategory;

   }
   public String[] addIngredients(){
      ingredientsArr=new String[] {strIngredient1,strIngredient2,strIngredient3,strIngredient4,strIngredient5,strIngredient6,strIngredient7,strIngredient8,strIngredient9,strIngredient10,
              strIngredient11,strIngredient12,strIngredient13,strIngredient14,strIngredient15,strIngredient16,strIngredient17,strIngredient18,strIngredient19,strIngredient20};
      return ingredientsArr;
   }
   public String[] addMeasurs(){
      measuresArr=new String[]{strMeasure1,strMeasure2,strMeasure3,strMeasure4,strMeasure5,strMeasure6,strMeasure7,strMeasure8,strMeasure9,strMeasure10,
              strMeasure11,strMeasure12,strMeasure13,strMeasure14,strMeasure15,strMeasure16,strMeasure17,strMeasure18,strMeasure19,strMeasure20};
      return measuresArr;
   }
}
