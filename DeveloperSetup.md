# Basic Setup (of Eclipse and Maven) #
  * If you have a current eclipse version, it should already come with m2e (Maven integration) built-in. If not, follow [these instructions](https://code.google.com/p/dkpro-core-asl/wiki/UserSetup) and restart eclipse
  * In order to connect your Maven installation (e.g. the one that comes with m2e) with our Maven infrastructure, add our public server to your Maven configuration. If you do not yet have a settings.xml file in your .m2 directory, use the one provided [here](https://dkpro-tc.googlecode.com/svn/wiki/misc/settings.xml). Otherwise, add the repositories from the same settings.xml to your existing configuration.
  * By default, access to snapshot artifacts is disabled in the settings.xml mentioned above. If you need access to snapshots, uncomment the respective line in the configuration. **Note: if you work with the latest snapshot version of DKPro TC (i.e. what you will get when you checkout the code from SVN), we strongly recommend to enable access to snapshot artifacts in your settings.xml, as you won't be able to access (potential) snapshot dependencies otherwise.**
# Checking out a snapshot #
  * Open the SVN Repositories perspective in Eclipse (Menu -> Window ->   Show View -> Other... -> SVN -> SVN Repositories)
  * Add a SVN repository with the URL http://dkpro-tc.googlecode.com/svn/
  * Expand the new repository node in the SVN Repositories view
  * Right-click on trunk and select Check out as Maven project
    * Note: if you do not see this menu item, make sure you have installed the Maven SCM handler for Subclipse.
  * (optional) Eclipse will create some projects now. We recommend to group these projects into a working set:
    * Select Next
    * Check Add project(s) to working set
    * Click More...
    * Click New...
    * Double-click Java
    * Enter the working set name DKPRO-TC
    * Click Finish
    * Click OK
    * Select the working set DKPRO-TC from the working set drop-down box
      * Note: when you are completely through with these and the following steps, remember to go to the Package Explorer view. There is a small triangular icon in its top right corner. Click on that and select Top   Level Elements -> Working Sets.
  * Click Finish.