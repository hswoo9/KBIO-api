package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblUserMsg is a Querydsl query type for TblUserMsg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblUserMsg extends EntityPathBase<TblUserMsg> {

    private static final long serialVersionUID = 1533910096L;

    public static final QTblUserMsg tblUserMsg = new QTblUserMsg("tblUserMsg");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final NumberPath<Long> dsptchUserSn = createNumber("dsptchUserSn", Long.class);

    public final StringPath expsrYn = createString("expsrYn");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath msgCn = createString("msgCn");

    public final NumberPath<Long> msgGroup = createNumber("msgGroup", Long.class);

    public final NumberPath<Long> msgSn = createNumber("msgSn", Long.class);

    public final StringPath msgTtl = createString("msgTtl");

    public final StringPath rcptnIdntyYn = createString("rcptnIdntyYn");

    public final NumberPath<Long> rcptnUserSn = createNumber("rcptnUserSn", Long.class);

    public final StringPath sndngYmd = createString("sndngYmd");

    public QTblUserMsg(String variable) {
        super(TblUserMsg.class, forVariable(variable));
    }

    public QTblUserMsg(Path<? extends TblUserMsg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblUserMsg(PathMetadata metadata) {
        super(TblUserMsg.class, metadata);
    }

}

