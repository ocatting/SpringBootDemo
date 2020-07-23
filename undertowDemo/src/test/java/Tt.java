import java.util.Collection;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface Tt<K,V> {

//    Long leftPushAll(K key, Collection<V> values);

    Long leftPushAll(K key, Collection<?> values);

    Long leftPushAll(K key, V... values);

}
