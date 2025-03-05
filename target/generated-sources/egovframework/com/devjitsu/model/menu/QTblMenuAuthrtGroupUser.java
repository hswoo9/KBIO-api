package egovframework.com.devjitsu.model.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTblMenuAuthrtGroupUser is a Querydsl query type for TblMenuAuthrtGroupUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMenuAuthrtGroupUser extends EntityPathBase<TblMenuAuthrtGroupUser> {

    private static final long serialVersionUID = -336263609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTblMenuAuthrtGroupUser tblMenuAuthrtGroupUser = new QTblMenuAuthrtGroupUser("tblMenuAuthrtGroupUser");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final DateTimePath<java.time.LocalDateTime> authrtGrntDt = createDateTime("authrtGrntDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> authrtGroupSn = createNumber("authrtGroupSn", Long.class);

    public final NumberPath<Long> authrtGroupUserSn = createNumber("authrtGroupUserSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final egovframework.com.devjitsu.model.user.QTblUser tblUser;

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblMenuAuthrtGroupUser(String variable) {
        this(TblMenuAuthrtGroupUser.class, forVariable(variable), INITS);
    }

    public QTblMenuAuthrtGroupUser(Path<? extends TblMenuAuthrtGroupUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTblMenuAuthrtGroupUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTblMenuAuthrtGroupUser(PathMetadata metadata, PathInits inits) {
        this(TblMenuAuthrtGroupUser.class, metadata, inits);
    }

    public QTblMenuAuthrtGroupUser(Class<? extends TblMenuAuthrtGroupUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tblUser = inits.isInitialized("tblUser") ? new egovframework.com.devjitsu.model.user.QTblUser(forProperty("tblUser")) : null;
    }

}

