# Package Identifier

A gradle plugin for generating package identifiers in your modules. 
This plugin isn't meant for direct usage in runtime code, but rather to help your libraries be module aware.

Every module in a project will generate a file that looks like this: 

```kotlin
     // Generated, do not modify!
     package com.trello.sample
     
     import com.trello.identifier.annotation.PackageId
     
     @PackageId(isDebug = false)
     class PackageIdentifier
     
```

and another like this:


```kotlin
     // Generated, do not modify!
     package com.trello.sample
     
     import com.trello.identifier.annotation.PackageId
     
     @PackageId(isDebug = true)
     class PackageIdentifier
     
```

Annotation processors can then use the enclosing package of the class annotated by `@PackageId` in order to generate files into module specific packages.
Additionally, parsing the `isDebug` field allows you to handle debug variants differently from release variants.

## Usage

```groovy
buildscript {
  repositories {
    mavenCentral()
    google()
   }
  dependencies {
    classpath 'com.trello.identifier:package-identifier-plugin:0.0.2'
  }
}
```

and then apply to your modules
```groovy
apply plugin: 'com.trello.identifier'

```


# Contributors

Pull requests, issues and comments welcome. For pull requests:

* Add tests for new features and bug fixes
* Follow the existing style
* Separate unrelated changes into multiple pull requests

See the existing issues for things to start contributing.

For bigger changes, make sure you start a discussion first by creating an issue and explaining the intended change.

Atlassian requires contributors to sign a Contributor License Agreement, known as a CLA. This serves as a record stating that the contributor is entitled to contribute the code/documentation/translation to the project and is willing to have it used in distributions and derivative works (or is willing to transfer ownership).

Prior to accepting your contributions we ask that you please follow the appropriate link below to digitally sign the CLA. The Corporate CLA is for those who are contributing as a member of an organization and the individual CLA is for those contributing as an individual.

* [CLA for corporate contributors](https://na2.docusign.net/Member/PowerFormSigning.aspx?PowerFormId=e1c17c66-ca4d-4aab-a953-2c231af4a20b)
* [CLA for individuals](https://na2.docusign.net/Member/PowerFormSigning.aspx?PowerFormId=3f94fbdc-2fbe-46ac-b14c-5d152700ae5d)
    
# License

Copyright (c) 2018 Atlassian and others. Apache 2.0 licensed, see LICENSE file.
