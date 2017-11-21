package name.ulbricht.chess.pgn;

import org.antlr.v4.runtime.tree.ParseTreeListener;

interface PGNDatabaseListener<T> extends ParseTreeListener {

    T getData();
}
