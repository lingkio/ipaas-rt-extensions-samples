## How to build and deploy extension to Syndesis

### Building extensions
A Syndesis extension is a maven based project and can be build by invoking Maven goals. A Syndesis extension has a reference to a parent *io.syndesis.extensions:syndesis-extension-parent:1.0.0*, which has been installed as a GitHub package in the repository [ipaas-rt-extension-module](https://github.com/lingkio/ipaas-rt-extension-module). This parent package can be used as a regular Maven dependency wherever required. Please refer to the README of that depository for instructions on how to install the parent and use it as dependency in Maven pom files.

### Deploying extensions
The Maven build produces a jAR, which is the Syndesis extesion. Upload this JAR in order to deploy the extension. For a detailed procedure of writing, deploying and using Syndesis extensions, please follow the following links. Please refer to the documentation partaining to _step_ extensions.

- [Extensions overview](https://syndesis.io/docs/extensions/whatis/)
- [How to create extensions](https://syndesis.io/docs/extensions/create/)
