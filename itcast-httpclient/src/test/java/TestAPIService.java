import cn.itcast.httpclient.APIService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestAPIService {
    @Test
    private void testGet() throws Exception {
        APIService apiService = new APIService();
        String url = "http://www.baidu.com";
        Map<String,Object> map = new HashMap<>();
        map.put("a","1");
        apiService.doGet(url,map);
    }
}
