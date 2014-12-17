#Install JBoss Forge

Download the JBoss Forge and follow the instructions. Follow this link http://forge.jboss.org/document/installation

#Install the VRaptor4 addon

Type "forge" to enter in the forge console. Now you need to install the VRaptor addon. In order to do that, type the
command:
 
```
addon-install-from-git --url https://github.com/asouza/vraptor4-forge

```

#Creating a new project

First you need to create a new project.
 
```
project-new --named nameOfYourProject
```

Now you can setup your VRaptor4 project.

```
vraptor-setup --javaeeEnv true
```
The option **javaeeEnv** is optional. 

#Creating controllers

Just type the follow command:

```
vraptor-controller --packageName br.com.caelum.blank.controllers --className HomeController

```