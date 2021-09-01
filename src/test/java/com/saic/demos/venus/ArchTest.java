package com.saic.demos.venus;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.saic.demos.venus");

        noClasses()
            .that()
            .resideInAnyPackage("com.saic.demos.venus.service..")
            .or()
            .resideInAnyPackage("com.saic.demos.venus.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.saic.demos.venus.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
