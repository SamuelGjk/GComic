package moe.yukinoneko.gcomic.database;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by SamuelGjk on 2016/5/9.
 */
public class GComicDB {

    private static final String DB_NAME = "gcomic.db";

    private static GComicDB mInstance;

    private final LiteOrm liteOrm;

    public static GComicDB getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GComicDB.class) {
                if (mInstance == null) {
                    mInstance = new GComicDB(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private GComicDB(Context context) {
        liteOrm = LiteOrm.newSingleInstance(context, DB_NAME);
        liteOrm.setDebugged(true);
    }

    /**
     * 插入一条记录
     *
     * @param t 实体类对象
     */
    public <T> Observable<Long> insert(final T t) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(liteOrm.insert(t, ConflictAlgorithm.Abort));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 插入所有记录
     *
     * @param list 实体类对象list
     */
    public <T> Observable<Integer> insertAll(final List<T> list) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.insert(list, ConflictAlgorithm.Abort));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询所有
     *
     * @param cls 要查询的对象类型
     * @return 查询结果集合
     */
    public <T> Observable<ArrayList<T>> queryAll(final Class<T> cls) {
        return Observable.defer(new Func0<Observable<ArrayList<T>>>() {
            @Override
            public Observable<ArrayList<T>> call() {
                return Observable.just(liteOrm.query(cls));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询 指定字段 的值并去除重复
     *
     * @param cls   要查询的对象类型
     * @param columns 字段名称
     * @return 查询结果集合
     */
    public <T> Observable<ArrayList<T>> querySpecificColumnsAndDistinct(final Class<T> cls, final String[] columns) {
        return Observable.defer(new Func0<Observable<ArrayList<T>>>() {
            @Override
            public Observable<ArrayList<T>> call() {
                return Observable.just(liteOrm.query(new QueryBuilder<>(cls).columns(columns).distinct(true)));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询 某字段 等于 value 的值
     *
     * @param cls   要查询的对象类型
     * @param where 字段名称
     * @param value 字段的值
     * @return 查询结果集合
     */
    public <T> Observable<ArrayList<T>> queryByWhere(final Class<T> cls, final String where, final String[] value) {
        return Observable.defer(new Func0<Observable<ArrayList<T>>>() {
            @Override
            public Observable<ArrayList<T>> call() {
                return Observable.just(liteOrm.query(new QueryBuilder<>(cls).where(where + "=?", value)));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询 某字段 等于 value 的值并按照指定列的值升序排列
     */
    public <T> Observable<ArrayList<T>> queryByWhereAndDesc(final Class<T> cls, final String where, final String[] value, final String column) {
        return Observable.defer(new Func0<Observable<ArrayList<T>>>() {
            @Override
            public Observable<ArrayList<T>> call() {
                return Observable.just(liteOrm.query(new QueryBuilder<>(cls).where(where + "=?", value).appendOrderDescBy(column)));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询 某字段 等于 value 的值  可以指定从1-20，就是分页
     */
    public <T> Observable<ArrayList<T>> queryByWhereLength(final Class<T> cls, final String where, final String[] value, final int start, final int length) {
        return Observable.defer(new Func0<Observable<ArrayList<T>>>() {
            @Override
            public Observable<ArrayList<T>> call() {
                return Observable.just(liteOrm.query(new QueryBuilder<T>(cls).where(where + "=?", value).limit(start, length)));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除一条记录
     */
    public <T> Observable<Integer> delete(final T t) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.delete(t));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除所有 某字段 等于 vlaue 的值
     */
    public <T> Observable<Integer> deleteWhere(final Class<T> cls, final String where, final String[] value) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.delete(new WhereBuilder(cls).where(where + "=?", value)));
            }
        }).subscribeOn(Schedulers.io());
    }

    public <T> Observable<Integer> deleteAll(final List<T> list) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.delete(list));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除所有
     */
    public <T> Observable<Integer> deleteAll(final Class<T> cls) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.deleteAll(cls));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 更新一条记录
     */
    public <T> Observable<Integer> update(final T t) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.update(t));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 更新多条记录
     */
    public <T> Observable<Integer> updateAll(final List<T> list) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.update(list));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 保存（插入 or 更新）一条记录
     */
    public <T> Observable<Long> save(final T t) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(liteOrm.save(t));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 保存（插入 or 更新）多条记录
     */
    public <T> Observable<Integer> saveAll(final List<T> list) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(liteOrm.save(list));
            }
        }).subscribeOn(Schedulers.io());
    }
}
