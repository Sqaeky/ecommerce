@ApplicationModule(
        allowedDependencies = {"shared", "user::api", "inventory::api", "catalog::api"}
)
package cz.baladee.ecommerce.cart;

import org.springframework.modulith.ApplicationModule;