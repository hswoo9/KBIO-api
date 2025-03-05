package egovframework.com.devjitsu.model.search;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblIntgSrch is a Querydsl query type for TblIntgSrch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblIntgSrch extends EntityPathBase<TblIntgSrch> {

    private static final long serialVersionUID = 1793798079L;

    public static final QTblIntgSrch tblIntgSrch = new QTblIntgSrch("tblIntgSrch");

    public final StringPath atchFileExtnNm = createString("atchFileExtnNm");

    public final StringPath atchFileNm = createString("atchFileNm");

    public final NumberPath<Long> atchFileSn = createNumber("atchFileSn", Long.class);

    public final StringPath cn = createString("cn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> intgSrchSn = createNumber("intgSrchSn", Long.class);

    public final StringPath knd = createString("knd");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final NumberPath<Long> menuSn = createNumber("menuSn", Long.class);

    public final NumberPath<Long> pstSn = createNumber("pstSn", Long.class);

    public final StringPath ttl = createString("ttl");

    public final StringPath url = createString("url");

    public QTblIntgSrch(String variable) {
        super(TblIntgSrch.class, forVariable(variable));
    }

    public QTblIntgSrch(Path<? extends TblIntgSrch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblIntgSrch(PathMetadata metadata) {
        super(TblIntgSrch.class, metadata);
    }

}

