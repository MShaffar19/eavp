/*
 * generated by Xtext 2.10.0
 */
package org.eclipse.january.geometry.xtext.serializer;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.january.geometry.xtext.services.OBJGrammarAccess;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AlternativeAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;

@SuppressWarnings("all")
public class OBJSyntacticSequencer extends AbstractSyntacticSequencer {

	protected OBJGrammarAccess grammarAccess;
	protected AbstractElementAlias match_Face___SolidusKeyword_1_1_0_SolidusKeyword_1_1_1_2_0_EIntParserRuleCall_1_1_1_2_1__q;
	protected AbstractElementAlias match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____a;
	protected AbstractElementAlias match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____p;
	protected AbstractElementAlias match_VertexSource_NORMALTerminalRuleCall_3_2_a;
	protected AbstractElementAlias match_VertexSource_NORMALTerminalRuleCall_3_2_p;
	protected AbstractElementAlias match_VertexSource___GKeyword_2_0_EStringParserRuleCall_2_1_q__q;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (OBJGrammarAccess) access;
		match_Face___SolidusKeyword_1_1_0_SolidusKeyword_1_1_1_2_0_EIntParserRuleCall_1_1_1_2_1__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getFaceAccess().getSolidusKeyword_1_1_0()), new TokenAlias(false, false, grammarAccess.getFaceAccess().getSolidusKeyword_1_1_1_2_0()), new TokenAlias(false, false, grammarAccess.getFaceAccess().getEIntParserRuleCall_1_1_1_2_1()));
		match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____a = new GroupAlias(true, true, new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getSKeyword_2_1_0()), new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getEIntParserRuleCall_2_1_1_0()), new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getOffKeyword_2_1_1_1())));
		match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____p = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getSKeyword_2_1_0()), new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getEIntParserRuleCall_2_1_1_0()), new TokenAlias(false, false, grammarAccess.getPolyShapeAccess().getOffKeyword_2_1_1_1())));
		match_VertexSource_NORMALTerminalRuleCall_3_2_a = new TokenAlias(true, true, grammarAccess.getVertexSourceAccess().getNORMALTerminalRuleCall_3_2());
		match_VertexSource_NORMALTerminalRuleCall_3_2_p = new TokenAlias(true, false, grammarAccess.getVertexSourceAccess().getNORMALTerminalRuleCall_3_2());
		match_VertexSource___GKeyword_2_0_EStringParserRuleCall_2_1_q__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getVertexSourceAccess().getGKeyword_2_0()), new TokenAlias(false, true, grammarAccess.getVertexSourceAccess().getEStringParserRuleCall_2_1()));
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (ruleCall.getRule() == grammarAccess.getEIntRule())
			return getEIntToken(semanticObject, ruleCall, node);
		else if (ruleCall.getRule() == grammarAccess.getEStringRule())
			return getEStringToken(semanticObject, ruleCall, node);
		else if (ruleCall.getRule() == grammarAccess.getNORMALRule())
			return getNORMALToken(semanticObject, ruleCall, node);
		return "";
	}
	
	/**
	 * EInt returns ecore::EInt:
	 * 	'-'? INT
	 * ;
	 */
	protected String getEIntToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return "";
	}
	
	/**
	 * EString returns ecore::EString:
	 * 	(STRING | ID | '.' | '/' | '\\' | ':')+
	 * ;
	 */
	protected String getEStringToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return "\"\"";
	}
	
	/**
	 * terminal NORMAL: 'vn' DOUBLE DOUBLE DOUBLE;
	 */
	protected String getNORMALToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return "vn.";
	}
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if (match_Face___SolidusKeyword_1_1_0_SolidusKeyword_1_1_1_2_0_EIntParserRuleCall_1_1_1_2_1__q.equals(syntax))
				emit_Face___SolidusKeyword_1_1_0_SolidusKeyword_1_1_1_2_0_EIntParserRuleCall_1_1_1_2_1__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____a.equals(syntax))
				emit_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____a(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____p.equals(syntax))
				emit_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____p(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_VertexSource_NORMALTerminalRuleCall_3_2_a.equals(syntax))
				emit_VertexSource_NORMALTerminalRuleCall_3_2_a(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_VertexSource_NORMALTerminalRuleCall_3_2_p.equals(syntax))
				emit_VertexSource_NORMALTerminalRuleCall_3_2_p(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_VertexSource___GKeyword_2_0_EStringParserRuleCall_2_1_q__q.equals(syntax))
				emit_VertexSource___GKeyword_2_0_EStringParserRuleCall_2_1_q__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Ambiguous syntax:
	 *     ('/' '/' EInt)?
	 *
	 * This ambiguous syntax occurs at:
	 *     vertexIndices+=EInt (ambiguity) (rule end)
	 *     vertexIndices+=EInt (ambiguity) vertexIndices+=EInt
	 */
	protected void emit_Face___SolidusKeyword_1_1_0_SolidusKeyword_1_1_1_2_0_EIntParserRuleCall_1_1_1_2_1__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('s' (EInt | 'off'))*
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) 'f' faces+=Face
	 *     faces+=Face (ambiguity) 'f' faces+=Face
	 *     faces+=Face (ambiguity) (rule end)
	 *     material=Material (ambiguity) 'f' faces+=Face
	 *     name=EString (ambiguity) 'f' faces+=Face
	 */
	protected void emit_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____a(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('s' (EInt | 'off'))+
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) (rule start)
	 *     material=Material (ambiguity) (rule end)
	 *     name=EString (ambiguity) (rule end)
	 */
	protected void emit_PolyShape___SKeyword_2_1_0___EIntParserRuleCall_2_1_1_0_or_OffKeyword_2_1_1_1____p(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     NORMAL*
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) ('g' EString?)? (ambiguity) 'v' vertices+=Vertex
	 *     (rule start) ('g' EString?)? (ambiguity) 'vt' textureCoordinates+=TextureVertex
	 *     materialFiles+=EString ('g' EString?)? (ambiguity) 'v' vertices+=Vertex
	 *     materialFiles+=EString ('g' EString?)? (ambiguity) 'vt' textureCoordinates+=TextureVertex
	 *     textureCoordinates+=TextureVertex (ambiguity) 'v' vertices+=Vertex
	 *     textureCoordinates+=TextureVertex (ambiguity) 'vt' textureCoordinates+=TextureVertex
	 *     textureCoordinates+=TextureVertex (ambiguity) (rule end)
	 *     vertices+=Vertex (ambiguity) 'v' vertices+=Vertex
	 *     vertices+=Vertex (ambiguity) 'vt' textureCoordinates+=TextureVertex
	 *     vertices+=Vertex (ambiguity) (rule end)
	 */
	protected void emit_VertexSource_NORMALTerminalRuleCall_3_2_a(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     NORMAL+
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) ('g' EString?)? (ambiguity) (rule start)
	 *     materialFiles+=EString ('g' EString?)? (ambiguity) (rule end)
	 */
	protected void emit_VertexSource_NORMALTerminalRuleCall_3_2_p(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('g' EString?)?
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) NORMAL* 'v' vertices+=Vertex
	 *     (rule start) (ambiguity) NORMAL* 'vt' textureCoordinates+=TextureVertex
	 *     (rule start) (ambiguity) NORMAL+ (rule start)
	 *     materialFiles+=EString (ambiguity) NORMAL* 'v' vertices+=Vertex
	 *     materialFiles+=EString (ambiguity) NORMAL* 'vt' textureCoordinates+=TextureVertex
	 *     materialFiles+=EString (ambiguity) NORMAL+ (rule end)
	 */
	protected void emit_VertexSource___GKeyword_2_0_EStringParserRuleCall_2_1_q__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}
