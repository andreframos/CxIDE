
package org.eclipse.cxide.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.cxide.Activator;
import org.eclipse.cxide.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Classe utilizada pelas preferências e realce de syntax quando necessitam
 * de uma cor 
 * @author andreramos
 *
 */
public class ColorManager {

	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);
	private IPreferenceStore store;

	public ColorManager() {
		store = Activator.getDefault().getPreferenceStore();
	}

	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			e.next().dispose();
	}

	public Color getColor(RGB rgb) {
		Color color = fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
	

	//Atualmente não são usadas mas estas cores podem ser usadas como cores default
	//nas preferências
	public RGB getDefaultColor() {
		return PreferenceConverter.getColor(store,
				PreferenceConstants.OTHERS_COLOR_NAME_PREFERENCE);
	}

	public RGB getStringColor() {
		return new RGB(0, 0, 128);
	}

	public RGB getCommentColor() {
		return new RGB(0, 0, 128);
	}

	public RGB getVariableColor() {
		return new RGB(0, 0, 128);
	}

	public RGB getBuiltinColor() {
		return new RGB(0, 128, 0);
	}

	public RGB getUserDefinedColor() {
		return new RGB(160, 160, 0);
	}

	public RGB getKeywordColor() {
		return new RGB(0, 0, 128);
	}

}
