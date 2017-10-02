package org.jkarsten.bakingapp.bakingapp.foodlist.ui;

import org.jkarsten.bakingapp.bakingapp.data.Food;

/**
 * Created by juankarsten on 8/30/17.
 */

public class FoodImageUtil {
    public static String getFoodImageURL(Food food) {
        if (food.getImage() != null && food.getImage().length() > 0) {
            return food.getImage();
        }

        String url = "https://static.pexels.com/photos/205961/pexels-photo-205961.jpeg";
        if (food.getName().equals("Brownies")) {
            url = "https://cdn.pixabay.com/photo/2014/11/28/08/03/brownie-548591_640.jpg";
        } else if (food.getName().equals("Yellow Cake")) {
            url = "https://cdn.pixabay.com/photo/2017/06/07/20/26/sweet-2381480_640.jpg";
        } else if (food.getName().equals("Cheesecake")) {
            url = "https://cdn.pixabay.com/photo/2016/11/29/11/38/blur-1869227_640.jpg";
        }

        if (food.getImage() == null || food.getImage().length() == 0) {
            food.setImage(url);
        }
        return url;
    }
}
