import java.util.*;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class TtImpl<K,V> implements Tt<K,V> {

//    @Override
//    public Long leftPushAll(K key, Collection<V> values) {
//        System.out.println("Collection:"+key);
//        return null;
//    }

    @Override
    public Long leftPushAll(K key, Collection<?> values) {
        System.out.println("Collection:"+key);
        return null;
    }

    @Override
    public Long leftPushAll(K key, V... values) {
        System.out.println("[]:"+key);
        return null;
    }

    public static void main(String[] args) {
        Tt<String,Object> tt = new TtImpl();
        Map<String, List<Object>> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add("aa");
        map.put("nihao",list);
        map.forEach((k,v)->{
            tt.leftPushAll(k,v);
        });
        tt.leftPushAll("1",list);
    }


}