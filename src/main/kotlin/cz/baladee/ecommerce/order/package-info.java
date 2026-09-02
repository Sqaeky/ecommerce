@ApplicationModule(
        allowedDependencies = {"shared", "cart::api", "inventory::api", "catalog::api", "user::api"}
)
package cz.baladee.ecommerce.order;

import org.springframework.modulith.ApplicationModule;