package egovframework.com.devjitsu.model.login;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QLettnemplyrinfoVO is a Querydsl query type for LettnemplyrinfoVO
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLettnemplyrinfoVO extends EntityPathBase<LettnemplyrinfoVO> {

    private static final long serialVersionUID = -772777239L;

    public static final QLettnemplyrinfoVO lettnemplyrinfoVO = new QLettnemplyrinfoVO("lettnemplyrinfoVO");

    public final StringPath areaNo = createString("areaNo");

    public final StringPath brthdy = createString("brthdy");

    public final StringPath crtfcDnValue = createString("crtfcDnValue");

    public final StringPath detailAdres = createString("detailAdres");

    public final StringPath emailAdres = createString("emailAdres");

    public final StringPath emplNo = createString("emplNo");

    public final StringPath emplyrId = createString("emplyrId");

    public final StringPath emplyrSttusCode = createString("emplyrSttusCode");

    public final StringPath esntlId = createString("esntlId");

    public final StringPath fxnum = createString("fxnum");

    public final StringPath groupId = createString("groupId");

    public final StringPath houseAdres = createString("houseAdres");

    public final StringPath houseEndTelno = createString("houseEndTelno");

    public final StringPath houseMiddleTelno = createString("houseMiddleTelno");

    public final StringPath ihidnum = createString("ihidnum");

    public final StringPath mbtlnum = createString("mbtlnum");

    public final StringPath ofcpsNm = createString("ofcpsNm");

    public final StringPath offmTelno = createString("offmTelno");

    public final StringPath orgnztId = createString("orgnztId");

    public final StringPath password = createString("password");

    public final StringPath passwordCnsr = createString("passwordCnsr");

    public final StringPath passwordHint = createString("passwordHint");

    public final StringPath pstinstCode = createString("pstinstCode");

    public final StringPath sbscrbDe = createString("sbscrbDe");

    public final StringPath sexdstnCode = createString("sexdstnCode");

    public final StringPath userNm = createString("userNm");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public final StringPath zip = createString("zip");

    public QLettnemplyrinfoVO(String variable) {
        super(LettnemplyrinfoVO.class, forVariable(variable));
    }

    public QLettnemplyrinfoVO(Path<? extends LettnemplyrinfoVO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLettnemplyrinfoVO(PathMetadata metadata) {
        super(LettnemplyrinfoVO.class, metadata);
    }

}

