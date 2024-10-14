package io.digitalfemsa;

import java.util.HashMap;
import locales.Lang;
import org.json.JSONObject;

/**
 *
 * @author L.Carlos
 */
public class DiscountLine extends Resource {
    public Order order;
    public String description;
    public String code;
    public String type;
    public Integer amount;
    public Boolean deleted;

    @Override
    public String instanceUrl() throws Error {
        if (id == null || id.length() == 0) {
            HashMap parameters = new HashMap();
            parameters.put("RESOURCE", this.getClass().getSimpleName());
            throw new Error(Lang.translate("error.resource.id", parameters, Lang.EN),
                    Lang.translate("error.resource.id_purchaser", parameters, Digitalfemsa.locale), null, null, null);
        }
        String base = this.order.instanceUrl();
        return base + "/discount_lines/" + id;
    }
    
    @Override
    public void update(JSONObject params) throws Error, ErrorList {
        super.update(params);
    }

    public DiscountLine delete() throws Error, ErrorList {
        return ((DiscountLine) this.delete(null, null));
    }
}
