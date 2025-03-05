package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblUserLgnHstry is a Querydsl query type for TblUserLgnHstry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblUserLgnHstry extends EntityPathBase<TblUserLgnHstry> {

    private static final long serialVersionUID = -1431686450L;

    public static final QTblUserLgnHstry tblUserLgnHstry = new QTblUserLgnHstry("tblUserLgnHstry");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lgnDt = createDateTime("lgnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> lgnHstrySn = createNumber("lgnHstrySn", Long.class);

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblUserLgnHstry(String variable) {
        super(TblUserLgnHstry.class, forVariable(variable));
    }

    public QTblUserLgnHstry(Path<? extends TblUserLgnHstry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblUserLgnHstry(PathMetadata metadata) {
        super(TblUserLgnHstry.class, metadata);
    }

}

