# Release to Maven Central #

Please follow the [DKPro Core Release Guide](http://code.google.com/p/dkpro-core-asl/wiki/ReleaseGuide).

## Caveats ##

  * during branching/releasing, the formatting of the headers in the POMs might change, and eventually make the mycila license header plugin check fail. To avoid this, deactivate the check during the release.
  * during the release, delete all internal repositories from your setting.xml to make sure that all dependencies can be resolved properly.