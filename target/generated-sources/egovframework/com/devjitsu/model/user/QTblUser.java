package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblUser is a Querydsl query type for TblUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblUser extends EntityPathBase<TblUser> {

    private static final long serialVersionUID = -1158065807L;

    public static final QTblUser tblUser = new QTblUser("tblUser");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath addr = createString("addr");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath daddr = createString("daddr");

    public final StringPath email = createString("email");

    public final StringPath emlRcptnAgreYn = createString("emlRcptnAgreYn");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath infoRlsYn = createString("infoRlsYn");

    public final StringPath joinYmd = createString("joinYmd");

    public final StringPath kornFlnm = createString("kornFlnm");

    public final NumberPath<Long> lgnFailNmtm = createNumber("lgnFailNmtm", Long.class);

    public final StringPath mblTelno = createString("mblTelno");

    public final StringPath mbrStts = createString("mbrStts");

    public final NumberPath<Long> mbrType = createNumber("mbrType", Long.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath smsRcptnAgreYn = createString("smsRcptnAgreYn");

    public final StringPath swtcYmd = createString("swtcYmd");

    public final StringPath userId = createString("userId");

    public final StringPath userPw = createString("userPw");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> whdwlDt = createDateTime("whdwlDt", java.time.LocalDateTime.class);

    public final StringPath zip = createString("zip");

    public QTblUser(String variable) {
        super(TblUser.class, forVariable(variable));
    }

    public QTblUser(Path<? extends TblUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblUser(PathMetadata metadata) {
        super(TblUser.class, metadata);
    }

}

