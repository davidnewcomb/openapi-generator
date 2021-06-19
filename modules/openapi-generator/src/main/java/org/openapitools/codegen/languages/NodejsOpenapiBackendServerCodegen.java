package org.openapitools.codegen.languages;

import java.io.File;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.DefaultCodegen;
import org.openapitools.codegen.SupportingFile;


public class NodejsOpenapiBackendServerCodegen extends DefaultCodegen implements CodegenConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodejsOpenapiBackendServerCodegen.class);

    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    public String getName() {
        return "nodejs-openapi-backend";
    }

    public String getHelp() {
        return "Generates a NodeJS Openapi Backend Server.";
    }

    public NodejsOpenapiBackendServerCodegen() {
        super();

        outputFolder = "generated-code" + File.separator + "nodejs-openapi-backend";
        modelTemplateFiles.put("model.mustache", ".js");
        apiTemplateFiles.put("api.mustache", ".js");
        embeddedTemplateDir = templateDir = "nodejs-openapi-backend";
        apiPackage = "Apis";
        modelPackage = "Models";
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));

        supportingFiles.add(new SupportingFile("package.mustache", "", "package.json"));
        supportingFiles.add(new SupportingFile("handlerbase.mustache", "Apis", "HandlerBase.js"));
        supportingFiles.add(new SupportingFile("openapi.mustache", "api", "openapi.yaml"));
        supportingFiles.add(new SupportingFile("index.mustache", "", "index.js"));
        supportingFiles.add(new SupportingFile("errors.mustache", "Apis", "Errors.js"));
    }
    
    @Override
    public String escapeUnsafeCharacters(String input) {
    	LOGGER.info("DJN: ----");
        LOGGER.info("DJN: escapeUnsafeCharacters:" + input);
        String s = input.replace("*/", "*_/").replace("/*", "/_*");
        LOGGER.info("DJN: escapeUnsafeCharacters:" + s);
        return s;
    }

    @Override
    public void preprocessOpenAPI(OpenAPI openAPI) {
        if (openAPI.getInfo() != null) {
            Info info = openAPI.getInfo();
            if (info.getTitle() != null) {
                // When info.title is defined, use it for projectName
                // used in package.json
                String projectName = info.getTitle()
                        .replaceAll("[^a-zA-Z0-9]", "-")
                        .replaceAll("^[-]*", "")
                        .replaceAll("[-]*$", "")
                        .replaceAll("[-]{2,}", "-")
                        .toLowerCase(Locale.ROOT) + "-api";
                this.additionalProperties.put("projectName", projectName);
                return;
            }
        }
        LOGGER.warn("DJN: openapi.yaml:info.title missing, needed to generate npm package");
    }

}
