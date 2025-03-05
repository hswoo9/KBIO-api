package egovframework.com.devjitsu.model.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblContent is a Querydsl query type for TblContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblContent extends EntityPathBase<TblContent> {

    private static final long serialVersionUID = -308414721L;

    public static final QTblContent tblContent = new QTblContent("tblContent");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath contsCn = createString("contsCn");

    public final NumberPath<Long> contsSn = createNumber("contsSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final NumberPath<Long> menuSn = createNumber("menuSn", Long.class);

    public QTblContent(String variable) {
        super(TblContent.class, forVariable(variable));
    }

    public QTblContent(Path<? extends TblContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblContent(PathMetadata metadata) {
        super(TblContent.class, metadata);
    }

}

