package oatboat.model.export;

public class ICMLConstant {

	static String mainFileHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<?aid style=\"50\" type=\"snippet\" readerVersion=\"6.0\" featureSet=\"257\" product=\"8.0(370)\" ?>\n"
			+ "<?aid SnippetType=\"InCopyInterchange\"?>\n";

	static String documentBlockHeader = "<Document DOMVersion=\"8.0\" Self=\"d\">\n";

	static String rootStyleGroups = "	<RootCharacterStyleGroup Self=\"u186\">\n"
			+ "		<CharacterStyle Self=\"CharacterStyle/$ID/[No character style]\" Imported=\"false\" Name=\"$ID/[No character style]\" />\n"
			+ "		<CharacterStyle Self=\"CharacterStyle/bolder\" Imported=\"false\" KeyboardShortcut=\"1 104\" Name=\"bolder\" FontStyle=\"Condensed\">\n"
			+ "			<Properties>\n" + "				<BasedOn type=\"string\">$ID/[No character style]</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n" + "			</Properties>\n"
			+ "		</CharacterStyle>\n"
			+ "		<CharacterStyle Self=\"CharacterStyle/underlined\" Imported=\"false\" KeyboardShortcut=\"1 101\" Name=\"underlined\" Underline=\"true\">\n"
			+ "			<Properties>\n" + "				<BasedOn type=\"string\">$ID/[No character style]</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n" + "			</Properties>\n"
			+ "		</CharacterStyle>\n" + "	</RootCharacterStyleGroup>\n"
			+ "	<RootParagraphStyleGroup Self=\"u184\">\n"
			+ "		<ParagraphStyle Self=\"ParagraphStyle/general text\" Name=\"general text\" Imported=\"false\" NextStyle=\"ParagraphStyle/general text\" KeyboardShortcut=\"0 0\" FontStyle=\"Light Condensed\" PointSize=\"6\" KerningMethod=\"$ID/Optical\" Ligatures=\"false\" AppliedLanguage=\"$ID/Czech\" Hyphenation=\"false\" SpaceAfter=\"1.700787401574803\" Justification=\"CenterAlign\">\n"
			+ "			<Properties>\n"
			+ "				<BasedOn type=\"object\">ParagraphStyle/$ID/NormalParagraphStyle</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n"
			+ "				<Leading type=\"unit\">5.5</Leading>\n"
			+ "				<AppliedFont type=\"string\">Myriad Pro</AppliedFont>\n"
			+ "				<BalanceRaggedLines type=\"enumeration\">VeeShape</BalanceRaggedLines>\n"
			+ "			</Properties>\n" + "		</ParagraphStyle>\n"
			+ "		<ParagraphStyle Self=\"ParagraphStyle/contents paragraph\" Name=\"contents paragraph\" Imported=\"false\" NextStyle=\"ParagraphStyle/nutritional paragraph\" KeyboardShortcut=\"0 0\" KerningMethod=\"$ID/Metrics\">\n"
			+ "			<Properties>\n"
			+ "				<BasedOn type=\"object\">ParagraphStyle/general text</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n"
			+ "				<AllNestedStyles type=\"list\">\n" + "					<ListItem type=\"record\">\n"
			+ "						<AppliedCharacterStyle type=\"object\">CharacterStyle/bolder</AppliedCharacterStyle>\n"
			+ "						<Delimiter type=\"enumeration\">AnyWord</Delimiter>\n"
			+ "						<Repetition type=\"long\">1</Repetition>\n"
			+ "						<Inclusive type=\"boolean\">true</Inclusive>\n" + "					</ListItem>\n"
			+ "				</AllNestedStyles>\n" + "			</Properties>\n" + "		</ParagraphStyle>\n"
			+ "		<ParagraphStyle Self=\"ParagraphStyle/expiry date\" Name=\"expiry date\" Imported=\"false\" NextStyle=\"ParagraphStyle/expiry date\" KeyboardShortcut=\"0 0\" PointSize=\"11\" KerningMethod=\"$ID/Optical\" Ligatures=\"false\" Tracking=\"30\" AppliedLanguage=\"$ID/Czech\" Hyphenation=\"false\" SpaceAfter=\"1.700787401574803\" Justification=\"CenterAlign\">\n"
			+ "			<Properties>\n" + "				<BasedOn type=\"string\">$ID/[No paragraph style]</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n"
			+ "				<Leading type=\"unit\">11</Leading>\n"
			+ "				<AppliedFont type=\"string\">ITC Officina Sans</AppliedFont>\n"
			+ "				<BalanceRaggedLines type=\"enumeration\">VeeShape</BalanceRaggedLines>\n"
			+ "			</Properties>\n" + "		</ParagraphStyle>\n"
			+ "		<ParagraphStyle Self=\"ParagraphStyle/$ID/NormalParagraphStyle\" Name=\"$ID/NormalParagraphStyle\" Imported=\"false\" NextStyle=\"ParagraphStyle/$ID/NormalParagraphStyle\" KeyboardShortcut=\"0 0\">\n"
			+ "			<Properties>\n" + "				<BasedOn type=\"string\">$ID/[No paragraph style]</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n"
			+ "				<BulletsFont type=\"string\">$ID/</BulletsFont>\n"
			+ "				<BulletsFontStyle type=\"enumeration\">Nothing</BulletsFontStyle>\n"
			+ "			</Properties>\n" + "		</ParagraphStyle>\n"
			+ "		<ParagraphStyle Self=\"ParagraphStyle/nutritional paragraph\" Name=\"nutritional paragraph\" Imported=\"false\" NextStyle=\"ParagraphStyle/nutritional paragraph\" KeyboardShortcut=\"0 0\" Tracking=\"10\" SpaceAfter=\"2.834645669291339\">\n"
			+ "			<Properties>\n"
			+ "				<BasedOn type=\"object\">ParagraphStyle/general text</BasedOn>\n"
			+ "				<PreviewColor type=\"enumeration\">Nothing</PreviewColor>\n"
			+ "				<AllNestedStyles type=\"list\">\n" + "					<ListItem type=\"record\">\n"
			+ "						<AppliedCharacterStyle type=\"object\">CharacterStyle/bolder</AppliedCharacterStyle>\n"
			+ "						<Delimiter type=\"enumeration\">AnyWord</Delimiter>\n"
			+ "						<Repetition type=\"long\">4</Repetition>\n"
			+ "						<Inclusive type=\"boolean\">true</Inclusive>\n" + "					</ListItem>\n"
			+ "				</AllNestedStyles>\n" + "			</Properties>\n" + "		</ParagraphStyle>\n"
			+ "	</RootParagraphStyleGroup>\n";

	static String storyHeader = "	<Story Self=\"u1e3\" AppliedTOCStyle=\"n\" TrackChanges=\"false\" StoryTitle=\"packaging-slozeni-45mm-OVE\" AppliedNamedGrid=\"n\">\n"
			+ "		<StoryPreference OpticalMarginAlignment=\"false\" OpticalMarginSize=\"12\" FrameType=\"TextFrameType\" StoryOrientation=\"Horizontal\" StoryDirection=\"LeftToRightDirection\" />\n"
			+ "		<InCopyExportOption IncludeGraphicProxies=\"true\" IncludeAllResources=\"false\" />\n";

	static String storyAndDocumentFooter = "\t</Story>\n" + "</Document>\n";
}
