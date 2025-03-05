package egovframework.com.devjitsu.model.bbs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblPstCmnt is a Querydsl query type for TblPstCmnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblPstCmnt extends EntityPathBase<TblPstCmnt> {

    private static final long serialVersionUID = -30734711L;

    public static final QTblPstCmnt tblPstCmnt = new QTblPstCmnt("tblPstCmnt");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath cmntCn = createString("cmntCn");

    public final NumberPath<Long> cmntGrp = createNumber("cmntGrp", Long.class);

    public final NumberPath<Long> cmntSeq = createNumber("cmntSeq", Long.class);

    public final NumberPath<Long> cmntStp = createNumber("cmntStp", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath prvtPswd = createString("prvtPswd");

    public final NumberPath<Long> pstCmntSn = createNumber("pstCmntSn", Long.class);

    public final NumberPath<Long> pstSn = createNumber("pstSn", Long.class);

    public final StringPath rlsEn = createString("rlsEn");

    public final NumberPath<Long> upPstCmntSn = createNumber("upPstCmntSn", Long.class);

    public QTblPstCmnt(String variable) {
        super(TblPstCmnt.class, forVariable(variable));
    }

    public QTblPstCmnt(Path<? extends TblPstCmnt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblPstCmnt(PathMetadata metadata) {
        super(TblPstCmnt.class, metadata);
    }

}

