package egovframework.com.devjitsu.model.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblPstCntnHstry is a Querydsl query type for TblPstCntnHstry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblPstCntnHstry extends EntityPathBase<TblPstCntnHstry> {

    private static final long serialVersionUID = 1516836576L;

    public static final QTblPstCntnHstry tblPstCntnHstry = new QTblPstCntnHstry("tblPstCntnHstry");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> cntnNmtm = createNumber("cntnNmtm", Long.class);

    public final NumberPath<Long> cntnSn = createNumber("cntnSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> trgtSn = createNumber("trgtSn", Long.class);

    public final StringPath trgtTblNm = createString("trgtTblNm");

    public QTblPstCntnHstry(String variable) {
        super(TblPstCntnHstry.class, forVariable(variable));
    }

    public QTblPstCntnHstry(Path<? extends TblPstCntnHstry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblPstCntnHstry(PathMetadata metadata) {
        super(TblPstCntnHstry.class, metadata);
    }

}

