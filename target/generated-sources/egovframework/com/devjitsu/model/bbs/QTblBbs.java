package egovframework.com.devjitsu.model.bbs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblBbs is a Querydsl query type for TblBbs
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblBbs extends EntityPathBase<TblBbs> {

    private static final long serialVersionUID = 1209464987L;

    public static final QTblBbs tblBbs = new QTblBbs("tblBbs");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath ansPsbltyYn = createString("ansPsbltyYn");

    public final StringPath atchFileKndNm = createString("atchFileKndNm");

    public final StringPath atchFileYn = createString("atchFileYn");

    public final StringPath bbsNm = createString("bbsNm");

    public final NumberPath<Long> bbsSn = createNumber("bbsSn", Long.class);

    public final StringPath bbsTypeNm = createString("bbsTypeNm");

    public final NumberPath<Integer> bscPstCnt = createNumber("bscPstCnt", Integer.class);

    public final StringPath cmntPsbltyYn = createString("cmntPsbltyYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath pstCtgryYn = createString("pstCtgryYn");

    public final StringPath rmrkCn = createString("rmrkCn");

    public final StringPath wrtrRlsYn = createString("wrtrRlsYn");

    public QTblBbs(String variable) {
        super(TblBbs.class, forVariable(variable));
    }

    public QTblBbs(Path<? extends TblBbs> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblBbs(PathMetadata metadata) {
        super(TblBbs.class, metadata);
    }

}

