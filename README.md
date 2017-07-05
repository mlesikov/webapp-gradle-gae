# webapp-gradle-gae

Simple GAE template for web application  - java8, gradle, kotlin


Deploy the app

Setup appengine-web.xml :
```xml
  <application>projectID</application>
  <version>version</version>
```

```sh
gradle clean appengineUpdate
```

demo : 
```
https://demo-dot-myproject.appspot.com/
```

---Development----

Run the DEV server 
```sh
gradle clean appengineRun
```

Stop the DEV server 
```
gradle clean appengineStop
```

Hugo : 

site source folder : www
site build folder : www/public
site template folder : www/theme

build site 
```sh
cd www
hugo
```

hugo dev server 
```sh
cd www
hugo server
```