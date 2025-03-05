package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblMvnEntMbr is a Querydsl query type for TblMvnEntMbr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMvnEntMbr extends EntityPathBase<TblMvnEntMbr> {

    private static final long serialVersionUID = 1561788145L;

    public static final QTblMvnEntMbr tblMvnEntMbr = new QTblMvnEntMbr("tblMvnEntMbr");

    public final StringPath aprvYn = createString("aprvYn");

    public final NumberPath<Long> mvnEntSn = createNumber("mvnEntSn", Long.class);

    public final StringPath sysMngrYn = createString("sysMngrYn");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblMvnEntMbr(String variable) {
        super(TblMvnEntMbr.class, forVariable(variable));
    }

    public QTblMvnEntMbr(Path<? extends TblMvnEntMbr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblMvnEntMbr(PathMetadata metadata) {
        super(TblMvnEntMbr.class, metadata);
    }

}

