package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCnslttMbr is a Querydsl query type for TblCnslttMbr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCnslttMbr extends EntityPathBase<TblCnslttMbr> {

    private static final long serialVersionUID = -1631455373L;

    public static final QTblCnslttMbr tblCnslttMbr = new QTblCnslttMbr("tblCnslttMbr");

    public final StringPath cnsltActv = createString("cnsltActv");

    public final StringPath cnsltArtcl = createString("cnsltArtcl");

    public final NumberPath<Long> cnsltFld = createNumber("cnsltFld", Long.class);

    public final StringPath cnsltSlfint = createString("cnsltSlfint");

    public final NumberPath<Integer> crrPrd = createNumber("crrPrd", Integer.class);

    public final StringPath jbpsNm = createString("jbpsNm");

    public final StringPath ogdpNm = createString("ogdpNm");

    public final StringPath rmrkCn = createString("rmrkCn");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblCnslttMbr(String variable) {
        super(TblCnslttMbr.class, forVariable(variable));
    }

    public QTblCnslttMbr(Path<? extends TblCnslttMbr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCnslttMbr(PathMetadata metadata) {
        super(TblCnslttMbr.class, metadata);
    }

}

