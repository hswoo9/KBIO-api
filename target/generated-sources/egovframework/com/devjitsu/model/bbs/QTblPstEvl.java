package egovframework.com.devjitsu.model.bbs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblPstEvl is a Querydsl query type for TblPstEvl
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblPstEvl extends EntityPathBase<TblPstEvl> {

    private static final long serialVersionUID = 1107389410L;

    public static final QTblPstEvl tblPstEvl = new QTblPstEvl("tblPstEvl");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> comCdSn = createNumber("comCdSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final NumberPath<Long> evlUserSn = createNumber("evlUserSn", Long.class);

    public final StringPath evlYmd = createString("evlYmd");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath impvOpnnCn = createString("impvOpnnCn");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final NumberPath<Long> pstEvlSn = createNumber("pstEvlSn", Long.class);

    public final NumberPath<Long> pstSn = createNumber("pstSn", Long.class);

    public QTblPstEvl(String variable) {
        super(TblPstEvl.class, forVariable(variable));
    }

    public QTblPstEvl(Path<? extends TblPstEvl> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblPstEvl(PathMetadata metadata) {
        super(TblPstEvl.class, metadata);
    }

}

