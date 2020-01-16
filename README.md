# Tutorial 

   Adding new application configuration
 
 First of all you should create new flavor for new application configuration. In this case open gradle.build (app)  and add this lines
 1. Add new flavor:
 
![flavor](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/flavors.png)
 
   ```java
     newFlavor {
                dimension = 'vendor'
                applicationId = 'com.ets.android.newFlavor'
                resConfigs 'res'
                manifestPlaceholders = [ google_map_key:"#google_map_key"]  //-> changed into actual value
                
            buildConfigField(TYPE_STRING, VARIABLE_API_DEV_SERVER_URL, SERVER_NEW_FALVOR_ENDPOINT_DEV)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_SERVER_DEV, SERVER_NEW_FALVOR_DEV)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_CLIENT_KEY_DEV, CLIENT_KEY_NEW_FALVOR_DEV)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_APP_ID_DEV, APP_ID_NEW_FALVOR_DEV)

            buildConfigField(TYPE_STRING, VARIABLE_API_SERVER_URL, SERVER_NEW_FALVOR_ENDPOINT_PROD)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_SERVER_PROD, SERVER_NEW_FALVOR_PROD)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_CLIENT_KEY_PROD, CLIENT_KEY_NEW_FALVOR_PROD)
            buildConfigField(TYPE_STRING, VARIABLE_PARSER_APP_ID_PROD, APP_ID_NEW_FALVOR_PROD)   
}
```
[Add above code ](https://gitlab.mobidev.biz/android/ets/blob/otrar/app/build.gradle#L72)

 [Add below code] (https://gitlab.mobidev.biz/android/ets/blob/otrar/tools/build-utils.gradle#L4) into build-utils.gradle /
 changed into actual values #someURL, #someKey
    ``` java
      //****NEW_FLAVOR****//

    SERVER_NEW_FLAVOR_ENDPOINT_DEV = "\"#someURL\""
    SERVER_NEW_FLAVORS_ENDPOINT_PROD = "\"#someURL""

    //    Parser for notification
    //    PROD
    SERVER_NEW_FLAVOR_PROD = "\"#someURL/\""
    CLIENT_KEY_NEW_FLAVOR_PROD = "\"#someKEY\""
    APP_ID_NEW_FLAVOR_PROD = "\"#someURL\""
    //    DEV
    SERVER_NEW_FLAVOR_DEV = "\"#someURL/\""
    CLIENT_KEY_NEW_FLAVOR_DEV = "\"#someURL\""
    APP_ID_NEW_FLAVOR_DEV = "\"#someKey\""
 ```
 Find network-security-config.xml and added there subdomains from your inserted urls
  <?xml version="1.0" encoding="utf-8"?>
       <network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">parse.kwiket.com</domain>
        <domain includeSubdomains="true">static.quicket.me</domain>
        <domain includeSubdomains="true">static.quicket.io</domain>
        <domain includeSubdomains="true">dev.kwiket.com</domain>
        <domain includeSubdomains="true">apa02.kwiket.com</domain>
        <domain includeSubdomains="true">#yourSubDomain</domain>
    </domain-config>
     </network-security-config>
 ```

[screenshots]: https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts
 2. Create new folder with flavor name in project structure
 
![package](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/new_package.png)

 add **google-services.json** in newFlavor folder.
 
 ![package](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/added_google.png)

[Move to **src** folder ](https://gitlab.mobidev.biz/android/ets/tree/otrar/app/src)
 3. Create path newFolder/res/values/config.xml in this file placed branded colors. 
 
![values](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/resources.png)

![config](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/config.png)

 
 The rule such structure- will be used resources that closer to definition project.
 
 4. If need replace some resource (icon, drawable, string ect.) repeat the 
folder structure in the flavor and put in the required resource there; during 
assembly and it will be used.

![config_compose](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/compose_config.png)

   ```java
   <?xml version="1.0" encoding="utf-8"?>
<resources>

    //Branding colors
    <string name="app_name" translatable="false"># name application</string>
    <color name="light_brand_color">#color</color>
    <color name="brand_color">#color</color>
    <color name="semi_transparent_brand_color">#color</color>
    <color name="dark_brand_color">#color</color>


    <color name="dark_brand_color_e2">#color</color>
    <color name="dark_brand_color_ff">#color</color>
    <color name="brand_color_00">#color</color>

    // Quick access gradient colors
    <color name="menu_theme_1_background_0">@color/brand_color</color>
    <color name="menu_theme_1_background_1">@color/dark_brand_color</color>
    <color name="menu_theme_1_background_2">#color</color>

    //Date picker view colors
    <color name="day_picker_color_1">@color/brand_color</color>
    <color name="day_picker_color_2">#color</color>

    //Text brand color
    <color name="brand_text_color">#color</color>
    <color name="vivid_brand_color">#color</color>

    //Indicators color
    <color name="circle_time_view_time_full">@color/brand_color</color>
    <color name="flight_list_item_selected_airport">@color/light_brand_color</color>
    <color name="circle_time_view_time">@color/flight_list_item_selected_airport</color>
    <color name="airplane_bg_color">@color/vivid_brand_color</color>
</resources>

 //Terms and Conditions
        <string name="terms_condition_url" translatable="false">#url</string>
   
    //Support email
        <string name="agency_email" translatable="false">feedback@#app.to</string> 
        
      <!--Advisor theme-->
              <string name="theme_advisor_brand" translatable="false">#theme_name</string>          

```
**Replae #color to actual color and #app_name to real application name; #url - url terms&condition html page;**
 **#app- application name; #theme_name- theme name for jet**

5. Create **drawable** folder in newFlavor package (there you can put resoucers for icons,  shapes)

![drawable](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/drawable.png)

6. Add ic_launcher_background.xml and ic_splash.png  into drawable folder newFavor:

![launcher_bg](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/launcher_bg.png)

  ```java
  
  <?xml version="1.0" encoding="utf-8"?>
<shape android:shape="rectangle"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#color" />
</shape>
  
  ```
 **Raplace #color to actual color**
 
 **chat_logo.png** add into drawable folders (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi) for differernts screen resolution 

![chat_logo](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/chat_logo.png)

## Launcher for differents configuration


In Manifest file we selected what kind icon should be used for launcher:

![selector_launch](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/selector_launcher.png)

![main_selector_launch](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/main_selector_launcher.png)

Put attention that this files placed in *mipmap-anydpi-v26* Selector will be used for all app cofiguration. We only give him resources from mipmap folder next flavor.
ic_launcher_background.xml we added early for our newFlavor, we need create mipmap (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi)
in structure of newFlavor folder and add ic_launcher_foreground.png resource.

![mipmap](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/add_mipmap_folder.png)

![mipmap_foreground](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/add_foreground_launcher.png)

Select **res** folder newFlavor, click right buton mouse and select New/Image Asset 
![generate_launcher_1](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/generate_launcher_1.png)

![generate_launcher_2](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/generate_launcher_2.png)

Studio generate png resources for different launchers:

![generate_launcher_3](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/generate_launcher_3.png)

## Finishing compose new app configuration 

At the end configuration you should obtain such a folders structure:

![finish](https://github.com/AntoninaLegkaya/500px/blob/master/screenshorts/new_flavor_finish.png)


## Conclusion

 Unique resources:
  
*  chat_logo.pnd
*  ic_splash.png
*  ic_launcher_foreground.png
*  ic_launcher_background.png
*  config.xml - there you should enter name app and brand colors
*  google-services.json
  
