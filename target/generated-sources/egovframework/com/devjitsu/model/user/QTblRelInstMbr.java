package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblRelInstMbr is a Querydsl query type for TblRelInstMbr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblRelInstMbr extends EntityPathBase<TblRelInstMbr> {

    private static final long serialVersionUID = 914791332L;

    public static final QTblRelInstMbr tblRelInstMbr = new QTblRelInstMbr("tblRelInstMbr");

    public final StringPath aprvYn = createString("aprvYn");

    public final NumberPath<Long> relInstSn = createNumber("relInstSn", Long.class);

    public final StringPath sysMngrYn = createString("sysMngrYn");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblRelInstMbr(String variable) {
        super(TblRelInstMbr.class, forVariable(variable));
    }

    public QTblRelInstMbr(Path<? extends TblRelInstMbr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblRelInstMbr(PathMetadata metadata) {
        super(TblRelInstMbr.class, metadata);
    }

}

