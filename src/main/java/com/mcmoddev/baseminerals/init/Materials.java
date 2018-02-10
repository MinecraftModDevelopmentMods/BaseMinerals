package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	protected Materials() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {

		final List<String> materials = Arrays.asList(MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT,
				MaterialNames.SALTPETER, MaterialNames.SULFUR);

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			createOrelessMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		materials.stream().filter(Options::isMaterialEnabled).forEach(name -> createMaterial(name, getMaterialType(name),
				getHardness(name), getStrength(name), getMagic(name), getColor(name)));

		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			createAlloyMaterial(MaterialNames.SILICON, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}
	}

	private static MaterialType getMaterialType(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return MaterialType.METAL;
			case MaterialNames.CHARCOAL:
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
			default:
				return MaterialType.MINERAL;
		}
	}

	private static int getColor(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
				return 0xFFC5C8C1;
			case MaterialNames.SILICON:
				return 0xFFC5C8C1;
			case MaterialNames.CHARCOAL:
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
			default:
				return 0xFF000000;
		}
	}

	private static double getMagic(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 4.5d;
			case MaterialNames.CHARCOAL:
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
			default:
				return 0d;
		}
	}

	private static double getStrength(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 8.0d;
			case MaterialNames.CHARCOAL:
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
				return 2.0d;
			default:
				return 1.0d;
		}
	}

	private static double getHardness(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 8d;
			case MaterialNames.CHARCOAL:
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
			default:
				return 1.0d;
		}
	}
}
