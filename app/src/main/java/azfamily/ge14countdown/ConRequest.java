package azfamily.ge14countdown;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faris on 7/12/2017.
 */

public class ConRequest extends StringRequest {

    private static final String CONFESS_REQUEST_URL = "http://pd101.xyz/azam/pru_submit2.php";
    private Map<String, String> params;

        public ConRequest(String choice ,String Semail ,String Sname , String Suid , Response.Listener<String> listener) {
            super(Method.POST, CONFESS_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("choice", choice);
            params.put("email", Semail);
            params.put("name", Sname);
            params.put("uid", Suid);
            }


        @Override
        public Map<String, String> getParams() {
            return params;
        }
}
