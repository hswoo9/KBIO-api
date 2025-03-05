package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblOrgcht is a Querydsl query type for TblOrgcht
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblOrgcht extends EntityPathBase<TblOrgcht> {

    private static final long serialVersionUID = -677360783L;

    public static final QTblOrgcht tblOrgcht = new QTblOrgcht("tblOrgcht");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final NumberPath<Long> deptSn = createNumber("deptSn", Long.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> jbttlSn = createNumber("jbttlSn", Long.class);

    public final StringPath kornFlnm = createString("kornFlnm");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final NumberPath<Long> orgchtSn = createNumber("orgchtSn", Long.class);

    public final NumberPath<Integer> sortSeq = createNumber("sortSeq", Integer.class);

    public final StringPath telno = createString("telno");

    public final StringPath tkcgTask = createString("tkcgTask");

    public QTblOrgcht(String variable) {
        super(TblOrgcht.class, forVariable(variable));
    }

    public QTblOrgcht(Path<? extends TblOrgcht> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblOrgcht(PathMetadata metadata) {
        super(TblOrgcht.class, metadata);
    }

}

