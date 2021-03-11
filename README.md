## How to build and deploy extension to Syndesis

### Building extensions
A Syndesis extension is a maven based project and can be build by invoking Maven goals. A Syndesis extension has a reference to a parent *io.syndesis.extensions:syndesis-extension-parent:1.0.0*, which is required for building it. **In their current state, the extensions in this repository will not build since they refer to a non-existing parent pom file**. As per Dale's suggestion, the parent will be built and installed in some internal repository and extensions built by Lingk and it's customers will refer to this parent. Once that is done the extensions will start building.

### Deploying extensions
The Maven build produces a jAR, which is the Syndesis extesion. Upload this JAR in order to deploy the extension. For a detailed procedure of writing, deploying and using Syndesis extensions, please follow the following links. Please refer to the documentation partaining to _step_ extensions.

- [Extensions overview](https://syndesis.io/docs/extensions/whatis/)
- [How to create extensions](https://syndesis.io/docs/extensions/create/)
